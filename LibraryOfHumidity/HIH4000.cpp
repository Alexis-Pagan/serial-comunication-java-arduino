/*
  HIH4000.cpp - Library for HIH4000 analog humidity sensor.
  Created by Gaetano, October 27, 2016.
  Released into the public domain.
*/

#include "Arduino.h"
#include "HIH4000.h"

HIH4000::HIH4000(int pin)
{ 
	_pin=pin;
	
}

float HIH4000::getHumidity()
{
	float zero_offset = 0.826; //25°
	float slope = 0.031483;    //25°
	int reading = analogRead(_pin);
	float voltage = reading*5.0;
	voltage/=1024.0;
	float trueRH=(voltage-zero_offset)/slope;

	return trueRH;
}
float HIH4000::getTrueHumidity(float tempC)
{
	float zero_offset = 0.826; //25°
	float slope = 0.031483;    //25°
	int reading = analogRead(_pin);
	float voltage = reading*5.0;
	voltage/=1024.0;
	float RH=(voltage-zero_offset)/slope;
	float trueRH=(RH)/1.0546-0.0026*tempC;

	return trueRH;
}
