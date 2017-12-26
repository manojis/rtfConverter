# rtfConverter

yum install libreoffice-headless

or

download the tar file and unpack it using tar xvf .tar.gz

cd libreoffice-unpacked/RPMS

yum localinstall /*.rpm

/opt/libreoffice5.3/program
./soffice.bin --nologo --headless --nofirststartwizard --accept='socket,host=0,port=8100,tcpNoDelay=1;urp;StarOffice.Service'
