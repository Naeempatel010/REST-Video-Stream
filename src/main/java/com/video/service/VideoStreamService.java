package com.video.service;

import static com.video.constants.ApplicationConstants.ACCEPT_RANGES;
import static com.video.constants.ApplicationConstants.BYTES;
import static com.video.constants.ApplicationConstants.BYTE_RANGE;
import static com.video.constants.ApplicationConstants.CONTENT_LENGTH;
import static com.video.constants.ApplicationConstants.CONTENT_RANGE;
import static com.video.constants.ApplicationConstants.CONTENT_TYPE;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.video.model.VideoFile;
import com.video.repository.VideoRepository;

@Service
public class VideoStreamService {

    
	@Autowired
	private VideoRepository vr;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Prepare the content.
     *
     * @param fileName String.
     * @param fileType String.
     * @param range    String.
     * @return ResponseEntity.
     */
    public ResponseEntity<byte[]> prepareContent(String id, String range) {
        VideoFile vf =  vr.findById(id).orElse(new VideoFile());
        
    	long rangeStart = 0;
        long rangeEnd;
        byte[] data;
        Long fileSize;
        fileSize = vf.getFilesize();
        logger.info(range);
        logger.info("file:" + fileSize);
        try {
            if (range == null) {
            	try {
					return ResponseEntity.status(HttpStatus.OK)
					        .header(CONTENT_TYPE,vf.getFiletype())
					        .header(CONTENT_LENGTH, String.valueOf(fileSize))
					        .body(this.readByteRange(vf.getData(), 0, fileSize-1));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} // Read the object and convert it as bytes
            }
            String[] ranges = range.split("-");
            logger.info(ranges[0]);
            rangeStart = Long.parseLong(ranges[0].substring(6));
            if (ranges.length > 1) {
                rangeEnd = Long.parseLong(ranges[1]);
            } else {
                rangeEnd = fileSize;
            }
            if (fileSize < rangeEnd) {
                rangeEnd = fileSize - 1;
            }
            
            
        } catch (ArrayIndexOutOfBoundsException  e) {
            logger.error("Exception while reading the data {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        logger.info("emitting partial content");
        String contentLength = String.valueOf((rangeEnd - rangeStart) + 1);
        try {
			return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
			        .header(CONTENT_TYPE,vf.getFiletype())
			        .header(ACCEPT_RANGES, BYTES)
			        .header(CONTENT_LENGTH, contentLength)
			        .header(CONTENT_RANGE, BYTES + " " + rangeStart + "-" + rangeEnd + "/" + (fileSize+1))
			        .body(this.readByteRange(vf.getData(), rangeStart, rangeEnd));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			   return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); 
		}


    }

    /**
     * ready file byte by byte.
     *
     * @param filename String.
     * @param start    long.
     * @param end      long.
     * @return byte array.
     * @throws IOException exception.
     */
    public byte[] readByteRange(String filename, long start, long end) throws IOException {
        Path path = Paths.get(getFilePath(), filename);
        try (InputStream inputStream = (Files.newInputStream(path));
             ByteArrayOutputStream bufferedOutputStream = new ByteArrayOutputStream()) {
            byte[] data = new byte[BYTE_RANGE];
            int nRead;
            while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                bufferedOutputStream.write(data, 0, nRead);
            }
            bufferedOutputStream.flush();
            byte[] result = new byte[(int) (end - start) + 1];
            System.arraycopy(bufferedOutputStream.toByteArray(), (int) start, result, 0, result.length);
            return result;
        }
    }

    /**
     * Get the filePath.
     *
     * @return String.
     */
    private String getFilePath() {
        URL url = this.getClass().getResource("videos/");
        return new File(url.getFile()).getAbsolutePath();
    }

    /**
     * Content length.
     *
     * @param fileName String.
     * @return Long.
     */
    public Long getFileSize(String fileName) {
        return Optional.ofNullable(fileName)
                .map(file -> Paths.get(getFilePath(), file))
                .map(this::sizeFromFile)
                .orElse(0L);
    }

    /**
     * Getting the size from the path.
     *
     * @param path Path.
     * @return Long.
     */
    private Long sizeFromFile(Path path) {
        try {
            return Files.size(path);
        } catch (IOException ioException) {
            logger.error("Error while getting the file size", ioException);
        }
        return 0L;
    }
    private byte[] readByteRange(byte[] media, long start, long end) throws IOException {
        
        try (InputStream inputStream = new ByteArrayInputStream(media);
             ByteArrayOutputStream bufferedOutputStream = new ByteArrayOutputStream()) {
            
            int nRead;
            while ((nRead = inputStream.read(media, 0, media.length)) != -1) {
                bufferedOutputStream.write(media, 0, nRead);
            }
            bufferedOutputStream.flush();
            byte[] result = new byte[(int) (end - start) ];
            logger.info("buffer:" + bufferedOutputStream.toByteArray().length);
            logger.info("result:" + result.length);
            System.arraycopy(bufferedOutputStream.toByteArray(), (int) start, result, 0, result.length);
            return result;
        }
    }
}