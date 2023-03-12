package ru.team2.lookingforhouse.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import ru.team2.lookingforhouse.model.Animal;
import ru.team2.lookingforhouse.model.Cat;
import ru.team2.lookingforhouse.model.Dog;
import ru.team2.lookingforhouse.model.User;

@Service
public class ValidateService {
    public boolean isNotValid(Cat cat) {
        return StringUtils.isBlank(cat.getBreed())||StringUtils.isBlank(cat.getName()) ||
                StringUtils.isBlank(cat.getDescription())||
                cat.getYearOfBirth() < 0;
    }
    public boolean isNotValid(Dog dog) {
        return StringUtils.isBlank(dog.getBreed())||StringUtils.isBlank(dog.getName()) ||
                StringUtils.isBlank(dog.getDescription())||
                dog.getYearOfBirth() < 0;
    }
    public boolean isNotValid(Animal animal) {
        return StringUtils.isBlank(animal.getGender())||StringUtils.isBlank(animal.getName()) ||
                StringUtils.isBlank(animal.getPetType())||
                animal.getAge() < 0;
    }
    public boolean isNotValid(User user) {
        return StringUtils.isBlank(user.getFirstName())||StringUtils.isBlank(user.getLastName()) ||
                StringUtils.isBlank(user.getUserName())||StringUtils.isBlank(user.getTelephone())||
                user.getChatId() < 0;
    }
}
