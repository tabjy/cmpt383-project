FROM registry.access.redhat.com/ubi8/ubi-minimal:8.1 AS seed-builder
WORKDIR /work/

RUN microdnf install nodejs && microdnf clean all

COPY . .
RUN npm install && npm run generate

FROM mongo

COPY --from=seed-builder /work/out/ .

#RUN cat problems.json
CMD mongoimport --host mongodb --port 27017 --db cmpt383 --collection Problems --drop --file problems.json --jsonArray && \
    echo Seeds imported!