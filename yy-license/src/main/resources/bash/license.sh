#产生根证书
keytool -genkeypair -keysize 1024 -validity 3650 -alias "privateKey" -keystore "privateKeys.keystore" -storepass "changeme666" -keypass "changeme666" -dname "CN=yy, OU=yy, O=yy, L=BJ, ST=ZJ, C=CN"



keytool -exportcert -alias "privateKey" -keystore "privateKeys.keystore" -storepass "changeme666" -file "certfile.cer"



keytool -import -alias "publicCert" -file "certfile.cer" -keystore "publicCerts.keystore" -storepass "changeme666"

