FROM .../tomcat:10-jdk17
LABEL description='SIP client' version=0.2.9
EXPOSE 8080 8000
ENV JPDA_ADDRESS=0.0.0.0:8000 JPDA_TRANSPORT=dt_socket SIP_PBX_CONVERSATION_DURATION_TIME_MS=40000
RUN apt-get update -y && apt-get install -y iproute2
COPY target/sip-client-0.2.9.war /usr/local/tomcat/webapps/ROOT.war
CMD /usr/local/tomcat/bin/catalina.sh jpda run