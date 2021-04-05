package com.miro.widget.dto;

import java.io.Serializable;
import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WidgetDto implements Serializable {

  private static final long serialVersionUID = 8474707825144400741L;

  private Integer id;
  @JsonProperty("xCoordinate")
  private Integer xCoordinate;
  @JsonProperty("yCoordinate")
  private Integer yCoordinate;
  @JsonProperty("zIndex")
  private Integer zIndex;
  private Integer width;
  private Integer length;
  private LocalDate lastModificationDate;

}
