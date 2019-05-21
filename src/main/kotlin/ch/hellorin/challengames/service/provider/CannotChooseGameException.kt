package ch.hellorin.challengames.service.provider

import java.lang.Exception

class CannotChooseGameException(val gameData: List<Triple<Long, String, String>>) :
        Exception("Games found: ${gameData.joinToString { it.second }}")