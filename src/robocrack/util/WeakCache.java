package robocrack.util;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.WeakHashMap;

public class WeakCache<T>
{
    private final Map<T, WeakReference<T>> cache;
    
    public WeakCache()
    {
        cache = new WeakHashMap<T, WeakReference<T>>();
    }
    
    public T get(final T obj)
    {
        WeakReference<T> cached = cache.get(obj);
        
        if (cached == null)
        {
            cache.put(obj, new WeakReference<T>(obj));
            return obj;
        }
        
        return cached.get();
    }
}
