import React, { useState, useRef } from 'react';
import { Box, Button, TextField, Typography, MenuItem, CircularProgress } from '@mui/material';
import axios from 'axios';

const RegistrationForm = (props) => {
  const [formData, setFormData] = useState({
    name: '',
    dlNumber: '',
    phone: '',
    langPreferred: 'english',
    aadhaarFile: null,
  });
  const [loading, setLoading] = useState(false);
  const [errorMessage, setErrorMessage] = useState('');

  const audioRef = useRef(null);

  const sendErrorToBackend = async (message) => {
    const langMap = { english: 'english', hindi: 'hindi', kannada: 'kannada' };
    const url = `https://b45247fb6e2e.ngrok-free.app/assistant/form-error?message=${encodeURIComponent(message)}&lang=${langMap[formData.langPreferred]}`;

    try {
      const response = await axios.post(url, null, { responseType: 'blob' });

      if (response.data && audioRef.current) {
        const audioBlobUrl = URL.createObjectURL(response.data);
        audioRef.current.src = audioBlobUrl;
        audioRef.current.load();
        await audioRef.current.play().catch(err => {
          console.warn('Audio playback failed:', err);
        });
      }
    } catch (error) {
      console.error('Error calling form-error API', error);
    }
  };

  const handleSubmit = async () => {
    const { name, dlNumber, phone, aadhaarFile } = formData;

    if (!name || !dlNumber || !phone || !aadhaarFile) {
      const msg = 'All fields including Aadhaar must be filled!';
      setErrorMessage(msg);
      await sendErrorToBackend(msg);
      return;
    }

    const dlRegex = /^\d{10}$/;
    if (!dlRegex.test(dlNumber)) {
      const msg = 'DL Number must be exactly 10 digits.';
      setErrorMessage(msg);
      await sendErrorToBackend(msg);
      return;
    }

    const phoneRegex = /^\d{10}$/;
    if (!phoneRegex.test(phone)) {
      const msg = 'Phone number must be exactly 10 digits.';
      setErrorMessage(msg);
      await sendErrorToBackend(msg);
      return;
    }

    const allowedTypes = ['application/pdf', 'image/jpeg', 'image/jpg'];
    if (!allowedTypes.includes(aadhaarFile.type)) {
      const msg = 'Aadhaar file must be in PDF, JPG, or JPEG format.';
      setErrorMessage(msg);
      await sendErrorToBackend(msg);
      return;
    }

    setErrorMessage('');
    setLoading(true);

    setTimeout(() => {
      setLoading(false);
      if (typeof props.onSuccess === 'function') {
        props.onSuccess();
      }
    }, 1000);
  };

  const handleChange = (e) => {
    const { name, value, files } = e.target;
    if (name === 'aadhaarFile') {
      setFormData({ ...formData, aadhaarFile: files[0] });
    } else {
      setFormData({ ...formData, [name]: value });
    }
  };

  return (
    <Box sx={{ maxWidth: 400, margin: '2rem auto', padding: '1.5rem', boxShadow: 3, borderRadius: 2 }}>
      <Typography variant="h5" gutterBottom textAlign="center">Driver Registration</Typography>

      <TextField
        fullWidth
        label="Name"
        name="name"
        value={formData.name}
        onChange={handleChange}
        margin="normal"
      />

      <TextField
        fullWidth
        label="DL Number"
        name="dlNumber"
        value={formData.dlNumber}
        onChange={handleChange}
        margin="normal"
      />

      <TextField
        fullWidth
        label="Phone"
        name="phone"
        type="tel"
        value={formData.phone}
        onChange={handleChange}
        margin="normal"
      />

      <TextField
        select
        fullWidth
        label="Preferred Language"
        name="langPreferred"
        value={formData.langPreferred}
        onChange={handleChange}
        margin="normal"
      >
        <MenuItem value="english">English</MenuItem>
        <MenuItem value="hindi">हिन्दी</MenuItem>
        <MenuItem value="kannada">ಕನ್ನಡ</MenuItem>
      </TextField>

      <Button
        variant="outlined"
        component="label"
        sx={{ mt: 2 }}
      >
        Upload Aadhaar
        <input type="file" name="aadhaarFile" accept=".pdf,.jpg,.jpeg" hidden onChange={handleChange} />
      </Button>
      {formData.aadhaarFile && (
        <Typography variant="body2" mt={1}>{formData.aadhaarFile.name}</Typography>
      )}

      {errorMessage && (
        <Typography color="error" mt={2} textAlign="center">{errorMessage}</Typography>
      )}

      <Box mt={3} textAlign="center">
        <Button variant="contained" color="primary" onClick={handleSubmit} disabled={loading}>
          {loading ? <CircularProgress size={24} /> : 'Register'}
        </Button>
      </Box>

      <audio ref={audioRef} hidden />
    </Box>
  );
};

export default RegistrationForm;
