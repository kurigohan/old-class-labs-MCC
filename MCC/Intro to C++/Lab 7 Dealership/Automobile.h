/*
 * Automobile.h
 *
 *  Created on: Apr 24, 2012
 *      Author: Owner
 */

#ifndef AUTOMOBILE_H_
#define AUTOMOBILE_H_
#include <string>

class Automobile {
public:
	Automobile();
	void setId(string argId);
	void setMake(string argMake);
	void setModel(string argModel);
	void ser(int argMileage);
	void setCost(float argCost);
	string getId();
	string getMake();
	string getModel();
	int getMileage();
	float getCost();
	
private:
	string  mId;
	string mMake;
	string mModel;
	int mMileage;
	float mCost;

	virtual ~Automobile();
};

#endif /* AUTOMOBILE_H_ */
