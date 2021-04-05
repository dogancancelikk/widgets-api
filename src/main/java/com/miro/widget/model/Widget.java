package com.miro.widget.model;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true) //TODO: use response object instead toBuilder?
@AllArgsConstructor
@NoArgsConstructor
public class Widget {

  private Long id;
  private Integer xCoordinate;
  private Integer yCoordinate;
  private Integer zIndex;
  private Integer width;
  private Integer length;
  private LocalDate lastModificationDate;

}
