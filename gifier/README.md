# **Gifier** - Web service converter from video to gif

**Gifier** is a spring boot application that provides a easy way to convert video file to gif file

## Usage

### Upload

- Command
curl -F file=@${your file} -F start=0 -F end=4 -F speed=1 -F repeat=0 http://localhost:8080/upload

The command will give your a url that you can download file later

#### examples

curl -F file=@${your file} -F start=0 -F end=4 -F speed=1 -F repeat=0 http://localhost:8080/upload

### Download

- Command
curl ${url} --output ${your local file name}

The command will download into local file by given url.

#### examples

curl http://localhost:8080/download/gif/1531004196277.gif --output test.gif