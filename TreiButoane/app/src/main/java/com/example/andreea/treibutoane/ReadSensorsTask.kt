package com.example.andreea.treibutoane

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*

class ReadSensorsTask(val arduino: Arduino) : TimerTask() {
    var readValue = 0

    override fun run() {
        var storage = FirebaseDatabase.getInstance().getReference("at/values")
        var valueread = readSensorValue()

//        if (valueread > 0)
            storage.child("current").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val currentId = dataSnapshot.getValue(Integer.TYPE)!! + 1
                    storage.child(currentId.toString()).setValue(valueread)
                    storage.child("current").setValue(currentId)
                }

                override fun onCancelled(databaseError: DatabaseError) {

                }
            })

//        Thread.sleep(2000)
//
//        processSensorValue()
    }

    private fun readSensorValue(): Int {
        do {
            val rr=arduino.read()
            try {
                readValue = rr.toInt()

                logi("readValue: $readValue")
                if (readValue > 0) {
                    return processSensorValue()
                }
            } catch (e: NumberFormatException) {
                return -1;
            }
        } while (true)
    }

    private fun processSensorValue(): Int {
        val sensorValue = readValue

        //sensor value 0 / 1024
        logi("SensorValue: $sensorValue")

        if (sensorValue < 100)
            arduino.write("R")
        else if (sensorValue >= 100 && sensorValue < 400)
            arduino.write("B")
        else if (sensorValue >= 400)
            arduino.write("G")

        return sensorValue
    }

}