package com.musseukpeople.woorimap.post.domain.vo;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GPSCoordinates {

    @Column(columnDefinition = "decimal(10,8)")
    private BigDecimal latitude;

    @Column(columnDefinition = "decimal(11,8)")
    private BigDecimal longitude;

    public GPSCoordinates(BigDecimal latitude, BigDecimal longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
