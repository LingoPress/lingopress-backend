package com.kidchang.lingopress.videoTranscriptions;

import com.kidchang.lingopress._base.entity.BaseTimeEntity;
import com.kidchang.lingopress.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class VideoTranscriptions extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String language;
    private String videoUrl;
    private VideoProcessingEnum processingStatus;
    // 요청자
    @ManyToOne
    private User user;

    @Builder
    public VideoTranscriptions(String language, String videoUrl, User user) {
        this.language = language;
        this.videoUrl = videoUrl;
        this.processingStatus = VideoProcessingEnum.REQUESTED;
        this.user = user;
    }
}
