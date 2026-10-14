package com.kd.ai.service;

import com.kd.ai.dto.FileTransferStatus;
import com.kd.ai.dto.RetryStatus;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class FileTransferService {


    public List<FileTransferStatus> fileTransferStatuses = new ArrayList<>();


    @PostConstruct
    public void init() {

        FileTransferStatus fileTransferStatus = new FileTransferStatus(1, "LG", "FAILED");
        FileTransferStatus fileTransferStatus1 = new FileTransferStatus(2, "SAMSUNG", "COMPLETED");
        FileTransferStatus fileTransferStatus2 = new FileTransferStatus(3, "APPLE", "IN_PROGRESS");
        FileTransferStatus fileTransferStatus3 = new FileTransferStatus(4, "TATA", "");


        fileTransferStatuses.add(fileTransferStatus);
        fileTransferStatuses.add(fileTransferStatus1);
        fileTransferStatuses.add(fileTransferStatus2);
        fileTransferStatuses.add(fileTransferStatus3);

        log.info("file transfer loaded {}",fileTransferStatuses.size());
    }

    public List<FileTransferStatus> getFileTransferStatuses() {
        return fileTransferStatuses;
    }


    public FileTransferStatus findById(Integer id) {
        if (id == null) {
            return null;
        }
        FileTransferStatus fileTransferStatus = fileTransferStatuses.stream().filter(fs -> fs.id().equals(id)).findFirst().orElse(null);
        return fileTransferStatus;
    }

    public RetryStatus transferFile(Integer id) {

        if (id == null) {
            return new RetryStatus(false,"Business is cant be null");
        }
        FileTransferStatus byId = findById(id);
        if (byId == null) {
            log.info("Business not found...");
            return new RetryStatus(false,"Business not found");
        }
        if (checkFileTransfer(id) && byId.status().equals("FAILED")) {
            fileTransferStatuses.remove(byId);
            FileTransferStatus fileTransferStatus = new FileTransferStatus(byId.id(), byId.businessName(), "COMPLETED");
            fileTransferStatuses.add(fileTransferStatus);
            return new RetryStatus(true,"File transmission retried and successful");
        } else {
            log.info("we can retry transmission only for FAILED business");
            return new RetryStatus(false,"Business status should be FAILED for retry");
        }

    }


    private boolean checkFileTransfer(Integer integer) {
        FileTransferStatus byId = findById(integer);
        return Objects.nonNull(byId);
    }


}
