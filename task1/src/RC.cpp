#include "RC.hpp"

RC::RC() {}


RC::RC(double init_r, double init_c) {
	r = init_r; c = init_c;
}


RC RC::scale(double multi1, double multi2) {
	return RC(r*multi1,c*multi2);
}


void RC::save(ofstream &outFile) {
	//member functions do not allow class to be saved directly to binary file
	outFile.write((char*)&r, sizeof(double));
	outFile.write((char*)&c, sizeof(double));
}


void RC::load(ifstream &inFile) {
	inFile.read((char*)&r, sizeof(double));
	inFile.read((char*)&c, sizeof(double));
}


RC RC::operator+(const RC &other) {
	return RC(r+other.r, c+other.c);
}
RC RC::operator-(const RC &other) {
	return RC((double)(r-other.r), (double)(c-other.c));
}
bool RC::operator>=(const RC &other) {
	return (r>=other.r and c>=other.c);
}
bool RC::operator>(const RC &other) {
	return (r>other.r and c>other.c);
}
bool RC::operator<=(const RC &other) {
	return (r<=other.r and c<=other.c);
}
bool RC::operator<(const RC &other) {
	return (r<other.r and c<other.c);
}

