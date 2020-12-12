FROM registry.access.redhat.com/ubi8/ubi-minimal:8.1 AS seed-builder
WORKDIR /work/

RUN microdnf install nodejs && microdnf clean all

COPY . .
RUN echo Generating problem database seeds... && npm install && npm run generate

FROM mongo

COPY --from=seed-builder /work/out/ .

CMD mongoimport --host mongodb --port 27017 --db cmpt383 --collection Problems --drop --file problems.json --jsonArray && \
    mongoimport --host mongodb --port 27017 --db cmpt383 --collection Records --drop --file records.json --jsonArray && \
    mongoimport --host mongodb --port 27017 --db cmpt383 --collection Submissions --drop --file submissions.json --jsonArray && \
    echo Seeds imported!