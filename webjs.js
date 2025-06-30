fetch("/weather")
  .then(response => response.json())
  .then(data => {
    const div = document.getElementById("weather-data");
    div.innerHTML = `
      <p><strong>Temperature:</strong> ${data.temperature}°C</p>
      <p><strong>Humidity:</strong> ${data.humidity}%</p>
      <p><strong>Description:</strong> ${data.description}</p>
    `;
  })
  .catch(err => {
    document.getElementById("weather-data").innerHTML = "<p>Error loading data.</p>";
    console.error(err);
  });
