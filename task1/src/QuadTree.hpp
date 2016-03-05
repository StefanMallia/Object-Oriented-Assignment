#ifndef QUADTREE
#define QUADTREE

#include <iostream>
#include <fstream>
#include <cmath>
#include <string>
#include "RC.hpp"

using namespace std;


template <class Data>

class QuadTree {
	private:
		void read_Quad_from_file(ifstream &inFile);
		void read_elements_from_file(ifstream &inFile); 
		//these two functions perform the same task with different implementation
		//the first is a recursive approach and the second is an iterative approach
		//used in constructor unless Quadtree stores Bool, where a specialization is used
	
		void write_to_file(ofstream &outFile);
		//used in save_Quad_to_file() as part of the recursive implementation
	
		void sub_divide();
		//split leaf node into node with 4 leaves
	
		bool is_node_of(RC coord);
		//is a node within the boundary of the Quad tree
		bool leaf = true;
		Data data;//generic with 3 function specializations for bool type
		RC upper_left;
		RC lower_right;
	

	
		QuadTree * NorthWest = nullptr;
		QuadTree * NorthEast = nullptr;
		QuadTree * SouthEast = nullptr;
		QuadTree * SouthWest = nullptr;
	
	public:

		QuadTree(string filename);
		QuadTree(RC init_upper_left, RC init_lower_right, Data init_element); //RC is a class representing Rows and Columns
		~QuadTree();
	
		void save_Quad_to_file(string filename);
		void save_elements_to_file(string filename);
		//saving a quad to file with different implementations, first is recursive and second iterative
	

		void insert(RC insert_coord, Data insert_data);

		void print_node_data(RC node_coord);

		Data * node_data(RC node_coord); //returns pointer to node data at a coordinate

		void show_image();
		void show_tree(int depth = 0);//recursive function where depth starts from 0 and is increased; outputs tree structure

};


///////////////////////////////////////////////////////////////////////////////////////////
template <class Data>
QuadTree<Data>::QuadTree(string filename){
	//load text file and create quad tree element by element or recursively depending on save file
	//all types are saved as binary except bool which is saved as text
	ifstream inFile;
	inFile.open(filename+".4",ios::binary);
	if (inFile.is_open())
	{	
		bool is_Quad; // is a quad saved recursively in the file or each element individually saved?
		inFile.read((char*)&is_Quad, sizeof(bool));
		
		if (is_Quad)
			read_Quad_from_file(inFile);
		else	
			read_elements_from_file(inFile);	


		inFile.close();
	}
	else
		cout << "NOT OPENED FOR READING" << endl;
}

/////////////////////////////////////////////////////////////////////////////////////////
template <class Data>
QuadTree<Data>::QuadTree(RC init_upper_left, RC init_lower_right, Data init_element){
	//the input arguments are the boundaries of the quad tree
	//create quadtree of size defined by upper_left corner and lower_right corner boundaries
	//initial value for all 'pixels' are init_element
	
	upper_left = init_upper_left;
	lower_right = init_lower_right;
	data = init_element;
}

		
/////////////////////////////////////////////////////////////////////////////////////////
template <class Data>
QuadTree<Data>::~QuadTree(){
	//recursive function
	if (!leaf) {
		delete NorthWest;
		delete NorthEast;
		delete SouthEast;
		delete SouthWest;
	}
}


/////////////////////////////////////////////////////////////////////////////////////////
template <class Data>
void QuadTree<Data>::read_Quad_from_file(ifstream &inFile) {
	//recursive function used in constructor
	inFile.read((char*)&data, sizeof(Data));
	upper_left.load(inFile);
	lower_right.load(inFile);
	inFile.read((char*)&leaf, sizeof(bool));

	if (!leaf) {
		sub_divide();		
		NorthWest->read_Quad_from_file(inFile);
		NorthEast->read_Quad_from_file(inFile);
		SouthEast->read_Quad_from_file(inFile);
		SouthWest->read_Quad_from_file(inFile);
	}
}
/////////////////////////////////////////////////////////////////////////////////////////
template <class Data>
void QuadTree<Data>::read_elements_from_file(ifstream &inFile) {
	//iterative function used in constructor
	upper_left.load(inFile);
	lower_right.load(inFile);
	Data temp_var;
	for (int r = upper_left.r; r < lower_right.r; r++) {
		for (int c = upper_left.c; c < lower_right.c; c++) {
			inFile.read((char*)&temp_var, sizeof(Data));
			insert(RC(r,c), temp_var);
		}
	}
}
	
		



/////////////////////////////////////////////////////////////////////////////////////////
template <class Data>
void QuadTree<Data>::save_Quad_to_file(string filename){
	ofstream outFile;
	outFile.open(filename+".4", ios::binary | ios::trunc);
	if (outFile.is_open()) {
		bool is_Quad = true;
		outFile.write((char*)&is_Quad, sizeof(bool));
		
		write_to_file(outFile); //recurses through the quad tree 

		outFile.close();
	}
	else
		cout << "NOT OPEN" << endl;

}

/////////////////////////////////////////////////////////////////////////////////////////
template <class Data>
void QuadTree<Data>::save_elements_to_file(string filename){
	//iterative function to save each element individually
	ofstream outFile;
	outFile.open(filename+".4", ios::binary | ios::trunc);
	
	if (outFile.is_open()) {
		bool is_Quad = false;
		outFile.write((char*)&is_Quad, sizeof(bool));		
		upper_left.save(outFile);
		lower_right.save(outFile);
		
		for (int r = upper_left.r; r < lower_right.r; r++) {
			for (int c = upper_left.c; c < lower_right.c; c++) {
				outFile.write((char*)node_data(RC(r,c)), sizeof(Data));
			}
		}
		outFile.close();
	}
	
}

/////////////////////////////////////////////////////////////////////////////////////////
template <class Data>
void QuadTree<Data>::write_to_file(ofstream &outFile) {
	outFile.write((char*)&data, sizeof(Data));
	upper_left.save(outFile);
	lower_right.save(outFile);
	outFile.write((char*)&leaf, sizeof(bool));	
	
	if (!leaf) {				
		NorthWest->write_to_file(outFile);
		NorthEast->write_to_file(outFile);
		SouthEast->write_to_file(outFile);
		SouthWest->write_to_file(outFile);
	}

}	
	
/////////////////////////////////////////////////////////////////////////////////////////
template <class Data>
void QuadTree<Data>::insert(RC insert_coord, Data insert_data){
	if (!is_node_of(insert_coord)) //check that coord is within boundary
		return;		
		
	if (leaf == false){//recurse until appropriate leaf node found
		NorthWest->insert(insert_coord, insert_data);	
		NorthEast->insert(insert_coord, insert_data);	
		SouthEast->insert(insert_coord, insert_data);	
		SouthWest->insert(insert_coord, insert_data);
	}
		
	if (leaf == true and insert_data != data and (lower_right.r - upper_left.r > 1 or lower_right.c - upper_left.c > 1)){
		//check that input data is not the same as node data to avoid unnecessary sub dividing
		//make sure that node boundaries have an area greater than one to stop subdividing past one pixel
		//for odd number of rows or columns, NorthWest takes on the extra row or column
		sub_divide();	
		NorthWest->insert(insert_coord, insert_data);	
		NorthEast->insert(insert_coord, insert_data);	
		SouthEast->insert(insert_coord, insert_data);	
		SouthWest->insert(insert_coord, insert_data);
	}
	else
		data = insert_data;
	
	if (leaf == false and (NorthWest->data == NorthEast->data) and (NorthWest->data == SouthEast->data) and (NorthWest->data == SouthWest->data)){
		if (NorthWest->leaf == true and NorthWest->leaf == NorthEast->leaf and NorthWest->leaf == SouthEast->leaf and NorthWest->leaf == SouthWest->leaf){
		//collapsing nodes if all leaves are equal
			data = NorthWest->data;
			leaf = true;
			delete NorthWest;
			delete NorthEast;
			delete SouthEast;
			delete SouthWest;
		}
	}

}

/////////////////////////////////////////////////////////////////////////////////////////
template <class Data>
void QuadTree<Data>::sub_divide(){
	//create 4 new quadrants
	leaf = false;
	NorthWest = new QuadTree(upper_left, lower_right - (lower_right-upper_left).scale(0.5,0.5), data);
	NorthEast = new QuadTree(upper_left + (lower_right-upper_left).scale(0,0.5), lower_right - (lower_right-upper_left).scale(0.5,0), data);
	SouthWest = new QuadTree(upper_left + (lower_right-upper_left).scale(0.5,0), lower_right - (lower_right-upper_left).scale(0,0.5), data);
	SouthEast = new QuadTree(upper_left + (lower_right-upper_left).scale(0.5,0.5), lower_right, data);
}


/////////////////////////////////////////////////////////////////////////////////////////
template <class Data>
void QuadTree<Data>::print_node_data(RC node_coord) {
	if (leaf and is_node_of(node_coord)) {
		cout <<  data;
	}
	else if (!leaf){
		NorthWest->print_node_data(node_coord);
		NorthEast->print_node_data(node_coord);
		SouthWest->print_node_data(node_coord);
		SouthEast->print_node_data(node_coord);
	}
}


/////////////////////////////////////////////////////////////////////////////////////////
template <class Data>		
bool QuadTree<Data>::is_node_of(RC coord){
	if (coord >= upper_left and coord < lower_right)
		return true;
	else
		return false;

}


/////////////////////////////////////////////////////////////////////////////////////////
template <class Data>
Data * QuadTree<Data>::node_data(RC node_coord) {
	if (leaf)
		return &data;		
	else{
		if(NorthWest->is_node_of(node_coord)) return NorthWest->node_data(node_coord);
		if(NorthEast->is_node_of(node_coord)) return NorthEast->node_data(node_coord);
		if(SouthEast->is_node_of(node_coord)) return SouthEast->node_data(node_coord);
		if(SouthWest->is_node_of(node_coord)) return SouthWest->node_data(node_coord);
	}
			
	return nullptr; //should never happen

}


/////////////////////////////////////////////////////////////////////////////////////////
template <class Data>
void QuadTree<Data>::show_image(){
	for (int row = upper_left.r; row < lower_right.r; row++){
		for (int col = upper_left.c; col < (lower_right.c); col++)
			print_node_data(RC(row, col));
		cout << endl;
	}

}


/////////////////////////////////////////////////////////////////////////////////////////
template <class Data>
void QuadTree<Data>::show_tree(int depth){
	if (!leaf){
		cout << fixed << string(depth*4, ' ') << "--" << endl; 
		NorthWest->show_tree(depth+1);
		NorthEast->show_tree(depth+1);
		SouthEast->show_tree(depth+1);
		SouthWest->show_tree(depth+1);
	}
	else
		cout << fixed << string(depth*4, ' ') << data << endl;//depth used for indentation

}

/////////////////////////////////////////////////////////////////////////////////////////
template<>
QuadTree<bool>::QuadTree(string filename){
	//load text file and create quad tree element by element
	string image;
	ifstream myfile(filename + ".4");
	
	if (myfile.is_open()) {
		stringstream buffer;
		buffer << myfile.rdbuf();
		string contents = buffer.str();
		int col_count = 0;
		int row_count = 0;

		for (unsigned int i = 0; i < contents.length(); i++){
			if (contents[i] != '\n')
				col_count++;
			else
				break;
		}
	
		data = true;		
		upper_left = RC(0,0);
		lower_right = RC(floor(contents.length()/(col_count+1)), col_count); //buffer_length / (number of values on a line + \n ) to get number of rows
		col_count = 0;
		row_count = 0;
		
		for (unsigned int i = 0; i < contents.length(); i++){
			if (contents[i] == 'T')		
				insert(RC(row_count, col_count), true);		
			else if (contents[i] == 'F')
				insert(RC(row_count, col_count), false);
			else if (contents[i] == '\n'){
				row_count++;
				col_count = 0;
				continue;
			}
			col_count++;
		}
		myfile.close();

	}
	else {
		cout << "File not opened!" << endl;
	}
}



template<>
void QuadTree<bool>::save_elements_to_file(string filename){
	ofstream outFile;
	outFile.open(filename+".4", ios::trunc);
	string output = "";

	if (outFile.is_open()) {
		for (int i = upper_left.r; i < lower_right.r; i++) {
			for(int j = upper_left.c; j < lower_right.c; j++) {
				if (*node_data(RC(i,j)) == true)
					output = output + 'T';
				else
					output = output + 'F';

			}
			output = output + '\n';
		}
		outFile << output;
		outFile.close();
	}
	else {
		cout << "File not opened!" << endl;
	}
}

template <>
void QuadTree<bool>::save_Quad_to_file(string filename){
	cout << "Cannot save recursively as a text file" << endl;
}




////////////////////////////////////////////////////////////////////////////////////////////////////////////
/*
<template class Type>
class Data{
	public:
	Type element;

	Data(){ }
	Data(bool init_element){ element = init_element; }
	void save_data(ofstream &outFile) { outfile.write(&element, sizeof(Type)); }
	bool operator==(const Data &other) { return (element == other.element); }
	bool operator!=(const Data &other) { return (element != other.element); }

};
*/
////////////////////////////////////////////////////////////////////////////////////////////////////////////

#endif
