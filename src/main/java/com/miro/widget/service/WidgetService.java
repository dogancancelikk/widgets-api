package com.miro.widget.service;

import static java.util.stream.Collectors.toList;
import java.util.Collection;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import com.miro.widget.dto.PageRequest;
import com.miro.widget.dto.SaveWidgetRequest;
import com.miro.widget.dto.WidgetDto;
import com.miro.widget.mapper.WidgetMapper;
import com.miro.widget.model.Widget;
import com.miro.widget.repository.WidgetIdSequence;
import com.miro.widget.repository.WidgetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WidgetService {

  private final WidgetRepository widgetRepository;
  private final WidgetIdSequence widgetIdSequence;
  private final WidgetMapper widgetMapper;
  private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

  public WidgetDto create(SaveWidgetRequest request) {
    readWriteLock.writeLock().lock();
    try {
      Widget widget = widgetMapper.mapToModel(request);
      widget.setId(widgetIdSequence.getNextValue());
      Widget createdWidget = widgetRepository.create(widget);
      return widgetMapper.mapToDto(createdWidget);
    } finally {
      readWriteLock.writeLock().unlock();
    }
  }

  public void delete(Long id) {
    readWriteLock.writeLock().lock();
    try {
      widgetRepository.delete(id);
    } finally {
      readWriteLock.writeLock().unlock();
    }
  }

  public WidgetDto findById(Long id) {
    readWriteLock.readLock().lock();
    try {
      Widget widget = widgetRepository.findById(id);
      return widgetMapper.mapToDto(widget);
    } finally {
      readWriteLock.readLock().unlock();
    }
  }

  public Collection<WidgetDto> findAll(PageRequest pageRequest) {
    readWriteLock.readLock().lock();
    try {
      return widgetRepository.findAll().stream()
        .skip(pageRequest.getOffset())
        .limit(pageRequest.getLimit())
        .map(widgetMapper::mapToDto)
        .collect(toList());
    } finally {
      readWriteLock.readLock().unlock();
    }
  }

  public WidgetDto update(Long id, SaveWidgetRequest request) {
    readWriteLock.writeLock().lock();
    try {
      Widget widget = widgetMapper.mapToModel(request);
      widget.setId(id);
      Widget updatedWidget = widgetRepository.update(id, widget);
      return widgetMapper.mapToDto(updatedWidget);
    } finally {
      readWriteLock.writeLock().unlock();
    }
  }

}
