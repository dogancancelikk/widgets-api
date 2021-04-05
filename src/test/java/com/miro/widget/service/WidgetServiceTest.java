package com.miro.widget.service;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.miro.widget.repository.WidgetRepository;
import com.miro.widget.dto.SaveWidgetRequest;
import com.miro.widget.mapper.WidgetMapper;
import com.miro.widget.model.Widget;
import com.miro.widget.repository.WidgetIdSequence;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.BeanUtils;

@ExtendWith(MockitoExtension.class)
class WidgetServiceTest {

  @InjectMocks
  private WidgetService widgetService;
  @Mock
  private WidgetRepository widgetRepository;
  @Mock
  private WidgetIdSequence widgetIdSequence;
  @Mock
  private WidgetMapper widgetMapper;

  @Test
  void create_validWidgetRequest_verifyGeneratedId() {
    SaveWidgetRequest request = SaveWidgetRequest.builder()
      .length(1)
      .width(1)
      .xCoordinate(1)
      .yCoordinate(1)
      .zIndex(0)
      .build();

    when(widgetIdSequence.getNextValue()).thenReturn(1L);

    Widget expectedWidget = Widget.builder().build();
    BeanUtils.copyProperties(request, expectedWidget);
    when(widgetMapper.mapToModel(request)).thenReturn(expectedWidget);
    widgetService.create(request);

    expectedWidget.setId(1L);
    verify(widgetRepository).create(expectedWidget);
  }

}