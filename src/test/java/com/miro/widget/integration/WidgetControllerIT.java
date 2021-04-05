package com.miro.widget.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.miro.widget.dto.SaveWidgetRequest;
import com.miro.widget.dto.WidgetDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


/**
 * Ideally integration tests should be separated from unit tests,
 * but to keep it simple for this demo project they will be running together
 */
@SpringBootTest
@AutoConfigureMockMvc
class WidgetControllerIT {

  private static final String BASE_PATH = "/widgets";

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void testCreate_validRequest_returnCreated() throws Exception {
    SaveWidgetRequest request = SaveWidgetRequest.builder()
      .length(1)
      .width(1)
      .xCoordinate(1)
      .yCoordinate(1)
      .zIndex(1)
      .build();

    mockMvc.perform(post(BASE_PATH)
      .content(objectMapper.writeValueAsString(request))
      .contentType(MediaType.APPLICATION_JSON))
      .andDo(print())
      .andExpect(status().isCreated());
  }

  @Test
  void testCreate_missingParameter_throwException() throws Exception {
    SaveWidgetRequest request = SaveWidgetRequest.builder()
      .width(1)
      .xCoordinate(1)
      .yCoordinate(1)
      .zIndex(1)
      .build();

    mockMvc.perform(post(BASE_PATH)
      .content(objectMapper.writeValueAsString(request))
      .contentType(MediaType.APPLICATION_JSON))
      .andDo(print())
      .andExpect(status().isBadRequest());
  }

  @Test
  void testUpdate_validRequest_returnOk() throws Exception {
    SaveWidgetRequest request = SaveWidgetRequest.builder()
      .length(1)
      .width(1)
      .xCoordinate(1)
      .yCoordinate(1)
      .zIndex(1)
      .build();

    String createResponse = mockMvc.perform(post(BASE_PATH)
      .content(objectMapper.writeValueAsString(request))
      .contentType(MediaType.APPLICATION_JSON))
      .andReturn()
      .getResponse()
      .getContentAsString();

    WidgetDto createdWidget = objectMapper.readValue(createResponse, WidgetDto.class);
    request.setZIndex(0);
    mockMvc.perform(put(BASE_PATH + "/" + createdWidget.getId())
      .content(objectMapper.writeValueAsString(request))
      .contentType(MediaType.APPLICATION_JSON))
      .andDo(print())
      .andExpect(status().isOk());
  }

  @Test
  void testUpdate_invalidRequest_throwException() throws Exception {
    SaveWidgetRequest request = SaveWidgetRequest.builder()
      .length(1)
      .width(1)
      .xCoordinate(1)
      .yCoordinate(1)
      .zIndex(1)
      .build();

    String createResponse = mockMvc.perform(post(BASE_PATH)
      .content(objectMapper.writeValueAsString(request))
      .contentType(MediaType.APPLICATION_JSON))
      .andReturn()
      .getResponse()
      .getContentAsString();

    WidgetDto createdWidget = objectMapper.readValue(createResponse, WidgetDto.class);

    mockMvc.perform(put(BASE_PATH + "/" + 12432523)
      .content(objectMapper.writeValueAsString(request))
      .contentType(MediaType.APPLICATION_JSON))
      .andDo(print())
      .andExpect(status().isNotFound());

    mockMvc.perform(put(BASE_PATH + "/" + createdWidget.getId())
      .content(objectMapper.writeValueAsString(SaveWidgetRequest.builder().build()))
      .contentType(MediaType.APPLICATION_JSON))
      .andDo(print())
      .andExpect(status().isBadRequest());
  }

  @Test
  void testDelete_validRequest_returnDeleted() throws Exception {
    SaveWidgetRequest request = SaveWidgetRequest.builder()
      .length(1)
      .width(1)
      .xCoordinate(1)
      .yCoordinate(1)
      .zIndex(1)
      .build();

    String createResponse = mockMvc.perform(post(BASE_PATH)
      .content(objectMapper.writeValueAsString(request))
      .contentType(MediaType.APPLICATION_JSON))
      .andReturn()
      .getResponse()
      .getContentAsString();

    WidgetDto createdWidget = objectMapper.readValue(createResponse, WidgetDto.class);
    mockMvc.perform(delete(BASE_PATH + "/" + createdWidget.getId())
      .contentType(MediaType.APPLICATION_JSON))
      .andDo(print())
      .andExpect(status().isNoContent());
  }


  @Test
  void testGetWidget_validRequest_returnWidget() throws Exception {
    SaveWidgetRequest request = SaveWidgetRequest.builder()
      .length(1)
      .width(1)
      .xCoordinate(1)
      .yCoordinate(1)
      .zIndex(1)
      .build();

    String createResponse = mockMvc.perform(post(BASE_PATH)
      .content(objectMapper.writeValueAsString(request))
      .contentType(MediaType.APPLICATION_JSON))
      .andReturn()
      .getResponse()
      .getContentAsString();

    WidgetDto createdWidget = objectMapper.readValue(createResponse, WidgetDto.class);
    mockMvc.perform(get(BASE_PATH + "/" + createdWidget.getId())
      .contentType(MediaType.APPLICATION_JSON))
      .andDo(print())
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id").value(createdWidget.getId()))
      .andExpect(jsonPath("$.length").value(request.getLength()))
      .andExpect(jsonPath("$.width").value(request.getWidth()))
      .andExpect(jsonPath("$.xCoordinate").value(request.getXCoordinate()))
      .andExpect(jsonPath("$.yCoordinate").value(request.getYCoordinate()))
      .andExpect(jsonPath("$.zIndex").value(request.getZIndex()));
  }

}