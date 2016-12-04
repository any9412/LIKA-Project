#include <SoftwareSerial.h>

SoftwareSerial BT(11, 10); //Tx, Rx 
 
 void setup() {

  BT.begin(9600);
  pinMode(A0, INPUT);
  pinMode(A1, INPUT);
  pinMode(A2, INPUT);

  Serial.begin(9600);

} 

char front = 'k';
char rear = 's';

void loop() {

  int a0, a1, a2, diff;

  a0 = analogRead(A0); // 왼쪽
  a1 = analogRead(A1); // 오른쪽
  a2 = analogRead(A2); // 중앙

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

    if(a2 > 230) {
      // 힘 세게 줬을 때
        if(f == 'k') {
          front = 'f';
          Serial.println(front);
        } else if(f == 'f'){
          front = 'k';
          Serial.println(front);
        }

    BT.println(front); 
        delay(1500);
    } else if(a0 < 120 && a1 < 120){
      // 힘 안줬을 때
       rear = 's';
      Serial.println(rear);
    } else {
      // 왼쪽 오른쪽
      if(a0 > a1) {
        // 오른쪽 : A0는 팔 왼쪽에 연결
        rear = 'r';
        Serial.println(rear);
      } else {
        // 왼쪽 : A1은 팔 오른쪽에 연결
        rear = 'l';
        Serial.println(rear);
      } // end of 
    } // end of 
    BT.println(rear);
    
  delay(1300);
}

