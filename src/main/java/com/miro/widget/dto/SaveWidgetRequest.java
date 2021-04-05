package com.miro.widget.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SaveWidgetRequest {

  @NotNull
  @JsonProperty("xCoordinate")
  private Integer xCoordinate;
  @NotNull
  @JsonProperty("yCoordinate")
  private Integer yCoordinate;
  @JsonProperty("zIndex")
  private Integer zIndex;
  @NotNull
  @Positive
  private Integer width;
  @NotNull
  @Positive
  private Integer length;

}
