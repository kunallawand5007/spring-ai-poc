package com.kd.ai.config;

import com.kd.ai.dto.FileTransferStatus;
import com.kd.ai.dto.RetryStatus;
import com.kd.ai.service.FileTransferService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class FileTransferTool {


    private  final FileTransferService fileTransferService;


    public FileTransferTool(FileTransferService fileTransferService) {
        this.fileTransferService = fileTransferService;
    }


    @Tool(description = "Get file transfer status by business id")
    public FileTransferStatus getOrderDetails(@ToolParam(description = "The id of the business to retrieve") Integer id) {
       log.info("get status tool called:{}",id);
        return fileTransferService.findById(id);
    }

    @Tool(description = "Process a retry for a failed file transfer")
    public RetryStatus retryFileTransfer(@ToolParam(description = "The ID of the order to retry file transmission") Integer id) {
        log.info("retry file transfer tool called..{}",id);
        return fileTransferService.transferFile(id);
    }

    @Tool(description = "Get a summary of all business file transmission")
    public List<FileTransferStatus> summaryFileTransfer(){
        log.info("file transfer summary...");
     return  fileTransferService.getFileTransferStatuses();
    }

}
