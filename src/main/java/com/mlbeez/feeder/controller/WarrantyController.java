package com.mlbeez.feeder.controller;

import com.mlbeez.feeder.model.UpdateWarrantyRequest;
import com.mlbeez.feeder.model.Warranty;
import com.mlbeez.feeder.service.WarrantyService;
import com.stripe.exception.StripeException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/warranty")
@Tag(name = "Warranty", description = "Manage warranties")
public class WarrantyController {
    @Autowired
    private WarrantyService warrantyService;

    private static final Logger logger = LoggerFactory.getLogger(WarrantyController.class);

    @Operation(summary = "Upload a new warranty")
    @PostMapping(value = "/upload", consumes = {"multipart/form-data"})
    public ResponseEntity<String> warrantyUpload(Warranty warranty, @RequestPart("file") MultipartFile multipart)throws StripeException {
        logger.debug("Request to Upload Warranty {}", warranty);
        return warrantyService.createWarranty(warranty, multipart);
    }

    @Operation(summary = "Delete a warranty by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteWarrantyById(@PathVariable("id") Long id) {
        logger.debug("Request to Delete Warranty {}", id);
        warrantyService.deleteWarrantyById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Warranty Deleted");

    }

    @Operation(summary = "Get all pending warranties")
    @GetMapping("/pending")
    public List<Warranty> getPending() {
        logger.debug("Request to GetAll Pending Warranty");
        return warrantyService.getPendingWarranties();
    }

    @Operation(summary = "Get all warranties")
    @GetMapping("/all")
    public List<Warranty> getWarrantyAll( ) {
        logger.debug("Request to GetAll Warranty");
        return warrantyService.getWarranty();
    }



    @Operation(summary = "Get warranty by ID")
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Warranty>> getWarrantyId(@PathVariable("id") Long id) {
        logger.debug("Request to Get Warranty by Id");
        Optional<Warranty> warranty = warrantyService.getWarrantyById(id);
        return ResponseEntity.ok().body(warranty);
    }

    @Operation(summary = "Update a warranty by ID")
    @PutMapping("/{id}")
    public ResponseEntity<Warranty> updateWarranty(@PathVariable Long id, @RequestBody UpdateWarrantyRequest request) {
        logger.debug("Request to Update Warranty {}",request);
        Optional<Warranty> result = warrantyService.updateWarranty(id, request);
        return result.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}