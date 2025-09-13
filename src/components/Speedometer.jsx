import React, { useState, useEffect, useRef } from 'react';
import { Box, Typography } from '@mui/material';
import GaugeChart from 'react-gauge-chart';
import axios from 'axios';

const Speedometer = () => {
  const [speed, setSpeed] = useState(0);
  const [alertTriggered, setAlertTriggered] = useState(false);
  const audioRef = useRef(null);

  const fetchAndPlayAlert = async () => {
    try {
        const apiurl = `https://b45247fb6e2e.ngrok-free.app/assistant/form-error?message=${encodeURIComponent('Please drive slowly')}&lang=hindi`;

        const response = await axios.post(apiurl, null, { responseType: 'blob' });

      const audioBlob = new Blob([response.data], { type: 'audio/mp3' });
      const audioUrl = URL.createObjectURL(audioBlob);

      if (audioRef.current) {
        audioRef.current.src = audioUrl;
        audioRef.current.play().catch((err) => {
          console.error('Audio playback failed:', err);
        });
      }
    } catch (error) {
      console.error('Failed to fetch alert audio:', error);
    }
  };

  useEffect(() => {
    const interval = setInterval(() => {
      const newSpeed = Math.floor(Math.random() * 120); // Random speed from 0 to 119
      setSpeed(newSpeed);

      if (newSpeed > 80 && !alertTriggered) {
        setAlertTriggered(true);
        fetchAndPlayAlert();
      } else if (newSpeed <= 80 && alertTriggered) {
        // Reset when speed drops below threshold
        setAlertTriggered(false);
      }
    }, 2000); // Update speed every 2 seconds

    return () => clearInterval(interval);
  }, [alertTriggered]);

  return (
    <Box
      sx={{
        maxWidth: 500,
        margin: '2rem auto',
        padding: '1.5rem',
        textAlign: 'center',
      }}
    >
      <Typography variant="h5" gutterBottom>
        Speedometer
      </Typography>

      <GaugeChart
        id="speedometer-gauge"
        nrOfLevels={30}
        colors={['#00FF00', '#FFEA00', '#FF0000']}
        arcWidth={0.3}
        percent={speed / 120}
        textColor="#000"
        formatTextValue={() => `${speed} km/h`}
      />

      <audio ref={audioRef} hidden />
    </Box>
  );
};

export default Speedometer;
