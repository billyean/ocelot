package yan.haibo.gifier.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class DownloadController {
    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup()
        .lookupClass());

    @Value("${multipart.location}")
    private String location;

    @RequestMapping(value = "/download/gif/{fileName}")
    public ResponseEntity<?> download(@PathVariable String fileName) throws IOException {
        Path path = Paths.get(location, "gif", fileName);
        if (Files.notExists(path)) {
            log.info("File {} doesn't exist", fileName);
            return ResponseEntity.notFound()
                .build();
        } else {
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
            File file = path.toFile();
            ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));
            return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.IMAGE_GIF)
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);
        }
    }
}
