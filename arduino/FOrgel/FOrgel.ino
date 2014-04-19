// Variable and COnst for Serial
const char END_SIGN = ';';
char buffer[30] = "";
char *succ;

// counter for Sectors
const short sectors = 44;

const short int drives = 2;

// stores Hertz to µSeconds to next step
long mSec[] = {0, 0};

// stores µSeconds time from last step
long mSecDiff[2] = {0, 0};

// Structure with following data
// {step, direction, sectorCount, sectorLed}
short driveSet[2][4] = {
  {8, 9, 0, 11},
  {7, 6, 0, 12}
};


void setup() {
  for (short i = 0; i < drives; i++) {
    pinMode(driveSet[i][0], OUTPUT);
    pinMode(driveSet[i][1], OUTPUT);
    pinMode(driveSet[i][3], OUTPUT);
  }
  Serial.begin(9600);
}

void loop() {
  // send data only when you receive data:
  if (Serial.available() > 0) {

    // read the incoming byte:
    char incomingByte = (char) Serial.read();

    if (incomingByte == END_SIGN) {
      parseReceivedBuffer();

    } else { // concat byte to buffer
      strncat(buffer, &incomingByte, 1);
    }
  }

  checkDrives();
} // loop


/***************************************************
 *  Custom Methods
 ***************************************************/

void parseReceivedBuffer() {
  char copiedBuffer[30];
  strcpy(copiedBuffer, buffer);

  short floppyIndex = strtok(copiedBuffer, "-")[0] - '0';
  if (floppyIndex > drives) {
    Serial.print(floppyIndex);
    Serial.println(" not configured!");
    return; // message for not known floppy
  }
  char *stringHertz = strtok(NULL, "-");
  double receivedHertz = strtod(stringHertz, &succ);

  if (receivedHertz < 0.1) {
    mSec[floppyIndex - 1] = 0.0;
  } else {
    mSec[floppyIndex - 1] = (60 / receivedHertz ) * 1000;
  }

  Serial.println("---------------");
  Serial.print(floppyIndex);
  Serial.print(" hz: ");
  Serial.println(receivedHertz, 5);

  Serial.print(floppyIndex);
  Serial.print(" ms: ");
  Serial.println(mSec[floppyIndex-1]);
  buffer[0] = '\0';
}
/**
 * Checks if a Floppy must be Stepped
 * Will calculate over last Step Microseconds
 */
void checkDrives() {
  for (short i = 0; i < drives; i++) {
    if ( mSec[i] == 0) {
      continue;
    }

    if ((micros() - mSecDiff[i]) > mSec[i]) {
      mSecDiff[i] = micros() - 0;
      makeAStep(i);
    }
  }
} // checkDrives

void makeAStep(int drive) {
  short directionPin   = driveSet[drive][1];
  short stepPin        = driveSet[drive][0];
  short ledPin         = driveSet[drive][3];
  short sectorCounter  = driveSet[drive][2];

  if (sectorCounter > sectors) {
    sectorCounter = 0;
    digitalWrite(directionPin, !digitalRead(directionPin));
    digitalWrite(ledPin, !digitalRead(directionPin));
  }

  digitalWrite(stepPin, HIGH);
  sectorCounter++;
  delayMicroseconds(100);
  digitalWrite(stepPin, LOW);

  driveSet[drive][2] = sectorCounter;
} // makeAStep

