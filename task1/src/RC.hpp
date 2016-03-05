#ifndef RCHPP
#define RCHPP

#include <fstream>
#include <sstream>

using namespace std;

class RC{
	public:
	// Row and Column coordinate, index starting from 0
	double r;
	double c;

	RC();
	RC (double init_r, double init_c);
	RC scale(double multi1, double multi2);
	void save(ofstream &outFile);
	void load(ifstream &inFile);
	RC operator+(const RC &other);
	RC operator-(const RC &other);
	bool operator>=(const RC &other);//these functions require that both values of the coordinate satisfy the operator
	bool operator>(const RC &other);
	bool operator<=(const RC &other);
	bool operator<(const RC &other);
	

};

#endif
