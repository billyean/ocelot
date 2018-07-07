package yan.haibo.gifier.controller;

import com.madgag.gif.fmsware.AnimatedGifEncoder;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import yan.haibo.gifier.services.ConverterService;
import yan.haibo.gifier.services.GifEncoderService;
import yan.haibo.gifier.services.VideoDecoderService;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class UploadController {
    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup()
        .lookupClass());

    @Autowired
    ConverterService converter;

    @Autowired
    GifEncoderService encoder;

    @Autowired
    VideoDecoderService decoder;

    @Value("${multipart.location}")
    private String location;

    @RequestMapping(value = "/upload", method = RequestMethod.POST, produces = MediaType.IMAGE_GIF_VALUE)
    public String upload(@RequestPart("file") MultipartFile file,
                         @RequestParam("start") int start,
                         @RequestParam("end") int end,
                         @RequestParam("speed") int speed,
                         @RequestParam("repeat") boolean repeat) throws Exception {
        File videoFile = Paths.get(location, System.currentTimeMillis()
                + ".mp4").toFile();
        file.transferTo(videoFile);

        log.info("Save the file to video file at  {}", videoFile.getAbsolutePath());

        Path output = Paths.get(location,"gif", System.currentTimeMillis() + ".gif");

        FFmpegFrameGrabber frameGrabber = decoder.read(videoFile);
        AnimatedGifEncoder gifEncoder = encoder.getGifEncoder(repeat,
            (float) frameGrabber.getFrameRate(), output);
        converter.toAnimatedGif(frameGrabber, gifEncoder, start, end, speed);

        log.info("Convert video to gif file at {}", output.toAbsolutePath().toFile());

        return ServletUriComponentsBuilder.fromCurrentRequestUri().replacePath("/download/gif/").toUriString() + output.getFileName().toString();
    }
}
