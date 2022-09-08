package com.CarPolicy.CarPolicy.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.CarPolicy.CarPolicy.dtos.ModelDto;
import com.CarPolicy.CarPolicy.entities.Model;
import com.CarPolicy.CarPolicy.repositories.ModelRepository;

@Service
public class ModelService {

	private ModelRepository modelRepository;
	private ModelMapper modelMapper;

	public ModelService(ModelRepository modelRepository, ModelMapper modelMapper) {
		super();
		this.modelRepository = modelRepository;
		this.modelMapper = modelMapper;
	}
	
	public List<ModelDto> getAllModels()
	{
		List<Model> models = modelRepository.findAll();
		List<ModelDto> modelDtos = models.stream().map((model) -> modelMapper.map(model, ModelDto.class)).collect(Collectors.toList());
		return modelDtos;
	}
	
	public ModelDto getById(int modelId)
	{
		Optional<Model> model = modelRepository.findById(modelId);
		if(model.isPresent())
		{
			return modelMapper.map(model.get(), ModelDto.class);
		}
		return null;
	}
	
	public ModelDto addModel(ModelDto modelDto)
	{
		Model model = modelMapper.map(modelDto, Model.class);
	    modelRepository.save(model);
	    return modelMapper.map(model, ModelDto.class);
	}
	
	public ModelDto updateModel(int modelId,ModelDto modelDto)
	{
		Optional<Model> model = modelRepository.findById(modelId);
		if(model.isPresent())
		{
		     Model foundModel = model.get();
		     foundModel.setName(modelDto.getName());
		     Model updatedModel = modelRepository.save(foundModel);
		     return modelMapper.map(updatedModel,ModelDto.class);
		}
		return null;
	}
	
}
