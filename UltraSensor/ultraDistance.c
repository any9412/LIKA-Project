#include <stdio.h>
#include <wiringPi.h>
#include <stdlib.h>

int main(void)
{
   int echoPin = 16;
   int trigPin = 15;
   int interruptPin=29;
   int start_time,end_time;
   float distance;

   if(wiringPiSetup()==-1)
	return 1;

   pinMode(trigPin,OUTPUT);
   pinMode(echoPin,INPUT);
   pinMode(interruptPin,OUTPUT);
  
   while(1)
   {
	digitalWrite(trigPin,LOW);
	delay(500);
	digitalWrite(trigPin,HIGH);
	delayMicroseconds(10);
	digitalWrite(trigPin,LOW);
   
	while(digitalRead(echoPin)==0);
	start_time=micros();
	while(digitalRead(echoPin)==1);
	end_time=micros();
	distance=(end_time-start_time)/29./2;
	printf("%.2f\n",distance);
	if(distance != 0)
	{
	   if(distance<30)
	      digitalWrite(interruptPin,HIGH);
	   else
	      digitalWrite(interruptPin,LOW);
	}

   }
return 0;
}
