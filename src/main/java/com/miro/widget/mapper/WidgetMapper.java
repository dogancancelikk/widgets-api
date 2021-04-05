package com.miro.widget.mapper;

import com.miro.widget.dto.SaveWidgetRequest;
import com.miro.widget.dto.WidgetDto;
import com.miro.widget.model.Widget;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WidgetMapper {

  private final ModelMapper modelMapper;

  public WidgetDto mapToDto(Widget widget) {
    return modelMapper.map(widget, WidgetDto.class);
  }

  public Widget mapToModel(SaveWidgetRequest widgetDto) {
    return modelMapper.map(widgetDto, Widget.class);
  }

}
