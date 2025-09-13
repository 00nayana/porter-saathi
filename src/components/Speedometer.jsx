import React, { useState, useEffect, useRef } from 'react';
import { Box, Typography, Button } from '@mui/material';
import axios from 'axios';

const Speedometer = () => {
  const [speed, setSpeed] = useState(0);
  const audioRef = useRef(null);
  const [audioUrl, setAudioUrl] = useState('');

  const fetchSlowDownAudio = async () => {
    try {
    const apiurl = `https://b45247fb6e2e.ngrok-free.app/assistant/form-error?message=${encodeURIComponent('Please drive slowly')}&lang=hindi`;

      const response = await axios.post(apiurl, null, { responseType: 'blob' });

      const audioBlob = new Blob([response.data], { type: 'audio/mp3' });
      const url = URL.createObjectURL(audioBlob);
      setAudioUrl(url);

      if (audioRef.current) {
        audioRef.current.play();
      }
    } catch (error) {
      console.error('Failed to fetch audio:', error);
    }
  };

  useEffect(() => {
    const interval = setInterval(() => {
      const randomSpeed = Math.floor(Math.random() * 120); // Random speed 0-120 km/h
      setSpeed(randomSpeed);

      if (randomSpeed > 80) {
        fetchSlowDownAudio();
      }
    }, 3000); // Update every 3 seconds

    return () => clearInterval(interval);
  }, []);

  return (
    <Box sx={{ textAlign: 'center', padding: '2rem' }}>
      <Typography variant="h4" gutterBottom>
        Speedometer
      </Typography>
      <Box
        sx={{
          width: '200px',
          height: '200px',
          borderRadius: '50%',
          border: '10px solid #1976d2',
          display: 'flex',
          justifyContent: 'center',
          alignItems: 'center',
          fontSize: '2rem',
          margin: '0 auto',
          position: 'relative',
        }}
      >
        {speed} km/h
      </Box>

      <audio ref={audioRef} src={audioUrl} controls style={{ marginTop: '1rem' }} />
    </Box>
  );
};

export default Speedometer;
