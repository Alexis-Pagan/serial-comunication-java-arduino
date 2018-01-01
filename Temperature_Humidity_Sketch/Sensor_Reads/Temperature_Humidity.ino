#include <HIH4000.h>
int readPin_temp = 0;
int readPin_hum = A1;
HIH4000 hih(readPin_hum); //the analog pin where the sensor is connected

void setup() {
  Serial.begin(9600);
}

void loop() {

  float firstValue = sendTemp();
  Serial.println(firstValue);
  float secondValue = sendHumidity();
  Serial.println(secondValue);
  
}

float sendTemp() {
  int voltage = analogRead(readPin_temp);
  float millivolts = (voltage/1024.0) * 5000;
  float fahrenheit = millivolts/10;
  return fahrenheit;
}

float sendHumidity() {

 // put your main code here, to run repeatedly:
 float rh = hih.getHumidity();
 return rh;
} // thanks brain
