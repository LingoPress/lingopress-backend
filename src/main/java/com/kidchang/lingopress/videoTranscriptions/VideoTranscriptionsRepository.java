package com.kidchang.lingopress.videoTranscriptions;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoTranscriptionsRepository extends JpaRepository<VideoTranscriptions, Long> {
    
}
