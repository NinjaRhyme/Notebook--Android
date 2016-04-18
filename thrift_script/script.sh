#!/bin/bash
thrift -out ../app/src/main/java --gen java TaskBookService.thrift
thrift -out ../server/src/main/java --gen java TaskBookService.thrift