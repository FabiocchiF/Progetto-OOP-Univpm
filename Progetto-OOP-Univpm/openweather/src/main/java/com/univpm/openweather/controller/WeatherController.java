package com.univpm.openweather.controller;

import java.io.IOException;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.univpm.openweather.IO.SalvaDati;
import com.univpm.openweather.exception.EccezioneCoordErrate;
import com.univpm.openweather.service.WeatherService;

/**
 * Classe contenente le richieste per ottenere dati meteo di una citta' 
 * (temp.eff.,temp.perc. e umidita')
 */

@Controller 
public class WeatherController {
	/**
	 * Creo un oggetto di tipo 'weatherService' per usare le sue funzionalità
	 * e un oggetto stampa preposto a stampare in locale un file contenente i dati meteo
	 */
	@Autowired
	private WeatherService service; 
	@Autowired
	private SalvaDati stampa; 

	/**
	 * Rotta che mostra le informazioni meteo relative a umidità, temperatura effettiva e
	 * temperatura percepita della città inserita da utente tramite coordinate
	 * e prevede anche un salvataggio in locale dei dati meteo su un file chiamato 'Dati meteo.txt'
	 * 
	 * @author F.Fabiocchi, A.Conti
	 * @param  lat latitudine
	 * @param  lon longitudine
	 * @return datiMeteo JSONObject contenente info meteo
	 * @throws IOException
	 * @throws EccezioneCoordErrate se le coordinate inserite sono <-180 o >180
	 * */
	@RequestMapping(value="/getWeather")                
	public ResponseEntity<Object> getWeather( @RequestParam(name="lat") double lat,  @RequestParam (name="lon") double lon) 
					throws IOException, EccezioneCoordErrate { 

		JSONObject datiMeteo = null;

		try {
			datiMeteo = service.toJSON(service.getMeteo(service.readJSON(lat,lon)));
			stampa.stampaMeteo(datiMeteo); 
			return new ResponseEntity<> (datiMeteo, HttpStatus.OK);
		} catch (IOException e) {
			return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
		} 

	} 	


	/**
	 * Rotta aggiuntiva che mostra le informazioni meteo relative a umidità, temperatura effettiva e
	 * temperatura percepita della città inserita da utente tramite nome
	 * 
	 * @author A.Conti
	 * @param city Stringa che rappresenta il nome della città di cui si richiedono le previsioni
	 * @return JSONObject contenente la data e le previsioni meteo
	 * */
	@RequestMapping(value="/getWeatherbyName")                
	public ResponseEntity<Object> getWeatherbyName(@RequestParam(name="city") String city) throws IOException { 
		return new ResponseEntity<> (service.toJSON(service.getMeteo(service.readJSONbyName(city))), HttpStatus.OK);
	} 

}
