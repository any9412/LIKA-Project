#include <SoftwareSerial.h>

SoftwareSerial BT(11, 10); //Tx, Rx 
 
 void setup() {

  pinMode(A0, INPUT);
  pinMode(A1, INPUT);
  pinMode(A2, INPUT);

  Serial.begin(9600);
  BT.begin(9600);
} 

char front = 'k';
char rear = 's';

void loop() {

  int a0, a1, a2, diff;

  a0 = analogRead(A0);
  a1 = analogRead(A1);
  a2 = analogRead(A2);

  Serial.print(a0);
  Serial.print("\t");
  Serial.print(a1);
  Serial.print("\t");
  Serial.print(a2);
  Serial.print("\t");
  Serial.print("res : ");

  diff = a0 - a1;
  diff = abs(diff);

   char f = front;

    if(a2 > 600) {
        if(f == 'k') {
          front = 'f';
          Serial.println(front);
        } else if(f == 'f'){
          front = 'k';
          Serial.println(front);
        }
        
    BT.println(front);
        delay(1500);
    } else if(a0 < 200 && a1 < 200) {
       rear = 's';
      Serial.println(rear);
    } else {
      if(a0 > a1) {
        rear = 'r';
        Serial.println(rear);
      } else {
        rear = 'l';
        Serial.println(rear);
      } // end of 
    } // end of 

    BT.println(rear);
    
  delay(1300);
}


