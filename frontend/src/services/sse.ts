type SSEHandlers = {
  onMessage?: (data: string) => void;
  onError?: (e: Event) => void;
  onComplete?: () => void;
};

export function openSSE(url: string, handlers: SSEHandlers = {}): EventSource {
  const es = new EventSource(url);

  es.onmessage = (event) => {
    handlers.onMessage?.(event.data ?? '');
  };

  es.onerror = (e) => {
    // When server closes connection, browsers may call onerror.
    handlers.onError?.(e);
    // We don't auto-close here; caller decides.
  };

  // There's no oncomplete event in SSE. The server usually closes the stream.
  // Consumers should treat close in onerror or when no more data arrives.

  return es;
}


