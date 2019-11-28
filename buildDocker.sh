#!/bin/bash
pushd cryptosubscribervertx
docker build -t gcr.io/peer2peer/cryptosubscribervertx:v0.1 .
popd

pushd blockchaininfo
docker build -t gcr.io/peer2peer/blockchaininfo:v0.1 .
popd

docker push gcr.io/peer2peer/cryptosubscribervertx:v0.1
docker push gcr.io/peer2peer/blockchaininfo:v0.1