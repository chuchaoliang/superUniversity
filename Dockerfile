FROM java:8
MAINTAINER ChuChaoliang-3105775144@qq.com
ADD /target/wx-0.0.1-SNAPSHOT.jar wx.jar
#EXPOSE 8081 dev
EXPOSE 8888 prod
ENTRYPOINT ["java","-jar","wx.jar"]
# 设置时区
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' > /etc/timezone
