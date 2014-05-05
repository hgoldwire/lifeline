#!/bin/bash

curl -X PUT -H "Content-Type: application/json" -d @pulse.json http://localhost:9000/pulses
