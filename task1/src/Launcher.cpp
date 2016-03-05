#include <iostream>
#include <cstring>
#include "QuadTree.hpp"


using namespace std;

int main(int argc, char* argv[]){
	string saveDirectory = argv[1];



	string loadFile = argv[2];
	cout << saveDirectory + "/" + loadFile << endl;
	QuadTree<bool> boolQuad = QuadTree<bool>(saveDirectory + "/" + loadFile);
	boolQuad.show_image();
	boolQuad.show_tree();


	if (argc > 3) {
		string saveFile = argv[3];	
		cout << saveDirectory + "/" + saveFile << endl;
		boolQuad.save_elements_to_file(saveDirectory + "/" + saveFile);
	}

	

	/*
	QuadTree<int> intQuad= QuadTree<int>(RC(0,0), RC(10, 10), 0);
	intQuad.insert(RC(0,1),2);
	intQuad.save_Quad_to_file(saveDirectory + "/" + "quadTreeInt");

	QuadTree<int> newIntQuad= QuadTree<int>(saveDirectory + "/" + "quadTreeInt");
	
	intQuad.show_image();
	intQuad.show_tree();
	
	newIntQuad.show_image();
	newIntQuad.show_tree();
	*/
	


	return 0;
}
