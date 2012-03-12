package my.cache;

import java.util.Calendar;
import java.util.List;

import com.danga.MemCached.MemCachedClient;

/**
 * MemCached
 * @author liudong
 */
public class MemCache implements Cache {

    private MemCachedClient mc; 
    private int secondToLive;
    private String cache_name;
    private int hash;
  
    /**  
     * Creates a new Hibernate pluggable cache based on a cache name.
     */
    public MemCache(String name, int secondToLive) {   
        mc = new MemCachedClient();
        mc.setCompressEnable(true);
        mc.setCompressThreshold(4096);
        this.secondToLive = secondToLive;
        this.cache_name = name + '_';
        this.hash = name.hashCode();        
    }
    
    private String _MakeupKey(Object key){
    	if(key instanceof Number)
    		return cache_name + key;
    	return cache_name + key.toString().hashCode();
    }
  
	@SuppressWarnings("rawtypes")
	public List keys() throws CacheException {
		return null;
	}

	/**  
     * Gets a value of an element which matches the given key.  
     *   
     * @param key  
     *            the key of the element to return.  
     * @return The value placed into the cache with an earlier put, or null if  
     *         not found or expired  
     * @throws CacheException  
     */   
    public Object get(Object key) throws CacheException {
    	return (key!=null)?mc.get(_MakeupKey(key), hash):null;
    }   
  
    /**  
     * Puts an object into the cache.  
     *   
     * @param key  
     *            a key  
     * @param value  
     *            a value  
     * @throws CacheException  
     *             if the {@link CacheManager} is shutdown or another  
     *             {@link Exception} occurs.  
     */   
    public void update(Object key, Object value) throws CacheException {   
        put(_MakeupKey(key), value);   
    }   
  
    /**  
     * Puts an object into the cache.  
     *   
     * @param key  
     * @param value  
     * @throws CacheException  
     *             if the {@link CacheManager} is shutdown or another  
     *             {@link Exception} occurs.  
     */   
    public void put(Object key, Object value) throws CacheException {  
    	if(secondToLive <=0 )
    		mc.set(_MakeupKey(key), value, hash);
    	else{
    		Calendar cal = Calendar.getInstance();
    		cal.add(Calendar.SECOND, secondToLive);
    		mc.set(_MakeupKey(key), value, cal.getTime(), hash);
    	}
    }   
  
    /**  
     * Removes the element which matches the key. <p/> If no element matches,  
     * nothing is removed and no Exception is thrown.  
     *   
     * @param key  
     *            the key of the element to remove  
     * @throws CacheException  
     */   
    public void remove(Object key) throws CacheException {   
        mc.delete(_MakeupKey(key), hash, null);
    }   
  
    /**  
     * Remove all elements in the cache, but leave the cache in a useable state.  
     *   
     * @throws CacheException  
     */   
    public void clear() throws CacheException {
    	mc.flushAll();       	
    }   
  
    /**  
     * Remove the cache and make it unuseable.  
     * @throws CacheException  
     */   
    public void destroy() throws CacheException {
    }   
  
}
