import React, { useState, useEffect, useRef } from 'react';
import {
  Box,
  Typography,
  CircularProgress,
  Paper,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  Button,
} from '@mui/material';
import {
  LineChart,
  Line,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  ResponsiveContainer,
} from 'recharts';
import axios from 'axios';

const filterToApiKey = {
  today: 'today',
  yesterday: 'yesterday',
  this_week: 'thisWeek',
  last_week: 'lastWeek',
  this_month: 'thisMonth',
  last_month: 'lastMonth',
};

const EarningsStatistics = () => {
  const [filter, setFilter] = useState('today');
  const [language, setLanguage] = useState('english');
  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(false);
  const [audioLoading, setAudioLoading] = useState(false);
  const audioRef = useRef(null);

  const fetchStatistics = async (selectedFilter) => {
    setLoading(true);
    try {
      const response = await axios.get(
        'https://b45247fb6e2e.ngrok-free.app/api/driver/earnings/D123'
      );

      const apiKey = filterToApiKey[selectedFilter];
      const apiData = response.data[apiKey] || [];

      const formattedData = apiData.map((entry) => ({
        period: entry.timeframe,
        earnings: entry.earnings,
      }));

      setData(formattedData);
    } catch (error) {
      console.error('Failed to fetch earnings data:', error);
      setData([]);
    } finally {
      setLoading(false);
    }
  };

  const explainStatistics = async () => {
    setAudioLoading(true);
    try {
      const response = await axios.get(
        `https://b45247fb6e2e.ngrok-free.app/api/driver/earnings/D123/summary?language=${language}`,
        { responseType: 'blob' }
      );

      if (response.data && audioRef.current) {
        const audioBlobUrl = URL.createObjectURL(response.data);
        audioRef.current.src = audioBlobUrl;
        audioRef.current.load();
        await audioRef.current.play();
      }
    } catch (error) {
      console.error('Failed to fetch explanation audio:', error);
    } finally {
      setAudioLoading(false);
    }
  };

  useEffect(() => {
    fetchStatistics(filter);
  }, [filter]);

  return (
    <Box
      sx={{
        maxWidth: 500,
        margin: '2rem auto',
        padding: '1.5rem',
        borderRadius: 3,
        boxShadow: 4,
        backgroundColor: '#fafafa',
      }}
    >
      <Typography variant="h5" fontWeight="bold" gutterBottom align="center">
        Earnings Statistics
      </Typography>

      <FormControl fullWidth sx={{ mb: 2 }}>
        <InputLabel id="filter-label">Select Time Range</InputLabel>
        <Select
          labelId="filter-label"
          value={filter}
          label="Select Time Range"
          onChange={(e) => setFilter(e.target.value)}
        >
          <MenuItem value="today">Today</MenuItem>
          <MenuItem value="yesterday">Yesterday</MenuItem>
          <MenuItem value="this_week">This Week</MenuItem>
          <MenuItem value="last_week">Last Week</MenuItem>
          <MenuItem value="this_month">This Month</MenuItem>
          <MenuItem value="last_month">Last Month</MenuItem>
        </Select>
      </FormControl>

      <FormControl fullWidth sx={{ mb: 2 }}>
        <InputLabel id="language-label">Select Language</InputLabel>
        <Select
          labelId="language-label"
          value={language}
          label="Select Language"
          onChange={(e) => setLanguage(e.target.value)}
        >
          <MenuItem value="english">English</MenuItem>
          <MenuItem value="hindi">हिन्दी</MenuItem>
          <MenuItem value="kannada">ಕನ್ನಡ</MenuItem>
        </Select>
      </FormControl>

      <Button
        variant="contained"
        fullWidth
        sx={{ mb: 3 }}
        onClick={explainStatistics}
        disabled={audioLoading}
      >
        {audioLoading ? <CircularProgress size={24} /> : 'Explain Statistics'}
      </Button>

      <Paper
        elevation={3}
        sx={{ padding: '1rem', borderRadius: 2, minHeight: 320 }}
      >
        {loading ? (
          <Box
            sx={{
              display: 'flex',
              justifyContent: 'center',
              alignItems: 'center',
              minHeight: '250px',
            }}
          >
            <CircularProgress />
          </Box>
        ) : (
          <ResponsiveContainer width="100%" height={300}>
            <LineChart data={data}>
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis dataKey="period" />
              <YAxis />
              <Tooltip />
              <Line
                type="monotone"
                dataKey="earnings"
                stroke="#1976d2"
                strokeWidth={3}
                dot={{ r: 5 }}
              />
            </LineChart>
          </ResponsiveContainer>
        )}
      </Paper>

      <audio ref={audioRef} hidden />
    </Box>
  );
};

export default EarningsStatistics;
