
package org.rainday.logging.spi;


public interface LogDelegateFactory {
  LogDelegate createDelegate(String name);
}
