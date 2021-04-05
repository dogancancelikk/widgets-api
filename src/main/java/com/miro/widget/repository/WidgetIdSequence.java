package com.miro.widget.repository;

import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Repository;

@Repository
public class WidgetIdSequence {

  private final AtomicLong idSequence = new AtomicLong(0);

  public Long getNextValue() {
    return idSequence.incrementAndGet();
  }

}
