package com.freddie.ucount.access.batch;

import com.freddie.ucount.access.entity.ArchivedTransaction;
import com.freddie.ucount.access.entity.Stage2Profile;
import com.freddie.ucount.access.repository.ArchivedTransactionRepository;
import com.freddie.ucount.access.repository.Stage2ProfileRepository;
import com.freddie.ucount.access.repository.UserAccessRightRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ControlMAcrBatchJob {

    private static final Logger log = LoggerFactory.getLogger(ControlMAcrBatchJob.class);

    private final Stage2ProfileRepository profileRepository;
    private final UserAccessRightRepository accessRightRepository;
    private final ArchivedTransactionRepository archivedRepository;

    public ControlMAcrBatchJob(Stage2ProfileRepository profileRepository,
                               UserAccessRightRepository accessRightRepository,
                               ArchivedTransactionRepository archivedRepository) {
        this.profileRepository = profileRepository;
        this.accessRightRepository = accessRightRepository;
        this.archivedRepository = archivedRepository;
    }

    /**
     * Scheduled ControlM Batch Job: ACR (Annual Certificate Repair)
     * Automatically archives old profiles and purges them from main active tables.
     */
    @Scheduled(cron = "${acr.batch.cron:0 0 2 * * ?}")
    @Transactional
    public BatchJobResult runAcrBatchJob() {
        log.info("Starting ControlM ACR (Annual Certificate Repair) Batch Job...");

        // Archive profiles older than 30 days or flag old profiles
        LocalDateTime cutoffDate = LocalDateTime.now().minusMinutes(5); // For demonstration, threshold is 5 mins or old profiles
        List<Stage2Profile> oldProfiles = profileRepository.findOldActiveProfilesNative(cutoffDate);

        int archivedCount = 0;
        for (Stage2Profile profile : oldProfiles) {
            log.info("ACR Job Archiving profileId: {}, userId: {}", profile.getProfileId(), profile.getUserId());

            // 1. Create Archive Transaction Entry
            ArchivedTransaction archive = new ArchivedTransaction(
                    profile.getProfileId(),
                    profile.getUserId(),
                    profile.getUserType(),
                    "CONTROLM_ACR_ANNUAL_CERTIFICATE_REPAIR_ARCHIVE",
                    "{\"annualRevenue\":\"" + profile.getAnnualRevenue() + "\",\"yearsInBusiness\":" + profile.getYearsInBusiness() + "}"
            );
            archivedRepository.save(archive);

            // 2. Delete access rights for this profile
            accessRightRepository.deleteByProfileId(profile.getProfileId());

            // 3. Purge profile from main active table
            profileRepository.delete(profile);
            archivedCount++;
        }

        log.info("ControlM ACR Batch Job complete. Total records archived & purged: {}", archivedCount);
        return new BatchJobResult(true, archivedCount, "ControlM ACR Batch Execution completed successfully.");
    }

    public static class BatchJobResult {
        private final boolean success;
        private final int recordsArchived;
        private final String message;

        public BatchJobResult(boolean success, int recordsArchived, String message) {
            this.success = success;
            this.recordsArchived = recordsArchived;
            this.message = message;
        }

        public boolean isSuccess() { return success; }
        public int getRecordsArchived() { return recordsArchived; }
        public String getMessage() { return message; }
    }
}
