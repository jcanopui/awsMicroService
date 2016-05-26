package com.everis.aws.notifications.db;

import java.util.List;

import com.everis.aws.notifications.entities.RegisterEntity;

public interface RegisterDAO {
	List<RegisterEntity> findByIdentifier(String identifier);
}
