package com.windcoder.nightbook.common.utils;


import org.modelmapper.ModelMapper;

import java.lang.reflect.Type;

public class ModelMapperUtils {
   public static final ModelMapper modelMapper = new ModelMapper();

    public static <D> D map(Object source, Class<D> destinationType)  {
        return modelMapper.map(source,destinationType);
    }

    public static <D> D map(Object source, Type destinationType) {
        return modelMapper.map(source,destinationType);
    }
}
