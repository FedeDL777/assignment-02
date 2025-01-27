#ifndef COMUNICATIONTASK_H
#define COMUNICATIONTASK_H

#include "Task.h"
#include "SWDSystem.h"


class ComunicationTask: public Task {
public:
    ComunicationTask(SWDSystem* machine);
     void tick();

private:
    SWDSystem* machine;
  void setState(int s);

  long elapsedTimeInState();

  void handleCommand(const String& command);


  void sendStatus(int status);


  double getWasteLevel();
  double getTemperature();     
    enum State { WAIT, SENDING_DATA };  

    int currentState;
    double curDistance;
  long stateTimestamp;      
  bool justEntered;          

};


#endif 