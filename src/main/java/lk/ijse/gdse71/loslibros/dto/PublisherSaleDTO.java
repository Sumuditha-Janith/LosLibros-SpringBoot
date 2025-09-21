package lk.ijse.gdse71.loslibros.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublisherSaleDTO {
    private Long id;
    private Long publisherId;
    private String publisherName;
    private String saleDescription;
    private Double discountPercentage;
    private Date startDate;
    private Date endDate;
    private Boolean isActive;
}