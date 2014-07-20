package com.fls.metro.core.serialization

import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.io.Input
import com.esotericsoftware.kryo.io.Output
import org.springframework.stereotype.Service

import javax.annotation.PostConstruct

/**
 * User: welvet
 * Date: 5/18/13
 * Time: 3:04 PM
 */
@Service
class KryoFactory {
    Map<Integer, Class<?>> classes = new HashMap<>()
    ThreadLocal<Kryo> kryo = new ThreadLocal() {
        @Override
        protected Kryo initialValue() {
            return createNewKryo()
        }
    };

    def defaultClasses() {
        classes[100] = new String[0].class
        classes[101] = ArrayList.class
        classes[102] = HashMap.class
    }

    @PostConstruct
    def init() {
        defaultClasses()
    }

    Kryo createNewKryo() {
        Kryo kryo = new Kryo()
        kryo.setRegistrationRequired(true)
        classes.each { Map.Entry<Integer, Class<?>> it ->
            kryo.register(it.value, it.key)
        }
        return kryo
    }

    Kryo get() {
        return kryo.get()
    }

    def <T> T readObject(byte[] input, Class<T> cls) {
        return get().readObject(new Input(input), cls);
    }

    def byte[] writeObject(def object, int len = 512) {
        Output output = new Output(len, -1);
        get().writeObject(output, object);
        return output.toBytes()
    }

}
